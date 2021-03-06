/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm;

import java.util.Collections;
import java.util.List;
import org.openstreetmap.josm.actions.downloadtasks.DownloadReferrersTask;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.PrimitiveId;
import org.openstreetmap.josm.gui.ExceptionDialogUtil;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.PleaseWaitRunnable;
import org.openstreetmap.josm.gui.io.DownloadPrimitivesTask;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.gui.progress.ProgressMonitor;
import org.openstreetmap.josm.gui.util.GuiHelper;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;


/**
 * Downloads an OSM way.
 *
 * @author nicoletav
 */
class DownloadWayTask extends PleaseWaitRunnable {

    private final PrimitiveId wayId;
    private final OsmDataLayer tempLayer;
    private boolean canceled;
    private PleaseWaitRunnable currentTask;


    DownloadWayTask(final PrimitiveId wayId) {
        super(GuiConfig.getInstance().getInfoMatchedWayTitle(), null, false);
        this.wayId = wayId;
        tempLayer = new OsmDataLayer(new DataSet(), OsmDataLayer.createNewName(), null);
    }


    @Override
    protected void cancel() {
        synchronized (this) {
            canceled = true;
            if (currentTask != null) {
                currentTask.operationCanceled();
            }
        }
    }


    @Override
    protected void finish() {
        synchronized (this) {
            if (canceled) {
                return;
            }
        }
        if (MainApplication.getLayerManager().getEditLayer() == null) {
            MainApplication.getLayerManager().addLayer(tempLayer);
        } else {
            MainApplication.getLayerManager().getEditLayer().mergeFrom(tempLayer);
        }
        GuiHelper.runInEDT(() -> MainApplication.getLayerManager().getEditDataSet().setSelected(wayId));

    }


    @Override
    protected void realRun() {
        downloadWay();
        downloadWayReferrers();
        currentTask = null;
    }

    private void downloadWay() {
        final List<PrimitiveId> ids = Collections.singletonList(wayId);
        final DownloadTask mainTask =
                new DownloadTask(tempLayer, ids, getProgressMonitor().createSubTaskMonitor(1, false));

        synchronized (this) {
            currentTask = mainTask;
            if (canceled) {
                currentTask = null;
                return;
            }
        }

        currentTask.run();
    }

    private void downloadWayReferrers() {
        synchronized (this) {
            if (canceled) {
                currentTask = null;
                return;
            }
            currentTask =
                    new DownloadReferrersTask(tempLayer, wayId, getProgressMonitor().createSubTaskMonitor(1, false));
        }
        currentTask.run();
    }

    private final class DownloadTask extends DownloadPrimitivesTask {

        public DownloadTask(final OsmDataLayer layer, final List<PrimitiveId> ids,
                final ProgressMonitor progressMonitor) {
            super(layer, ids, true, progressMonitor);
        }

        @Override
        protected void finish() {
            if (canceled) {
                return;
            }
            if (lastException != null) {
                ExceptionDialogUtil.explainException(lastException);
                return;
            }
            GuiHelper.runInEDTAndWait(() -> {
                layer.mergeFrom(ds);
                layer.onPostDownloadFromServer();
            });
        }
    }
}