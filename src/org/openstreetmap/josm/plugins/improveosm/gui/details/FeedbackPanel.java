/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details;

import static org.openstreetmap.josm.tools.I18n.tr;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.URI;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.tools.OpenBrowser;
import com.telenav.josm.common.gui.builder.ContainerBuilder;
import com.telenav.josm.common.gui.builder.TextComponentBuilder;


/**
 * Builds a panel for displaying the feedback information.
 *
 * @author Beata
 * @version $Revision$
 */
class FeedbackPanel extends JPanel implements HyperlinkListener {

    private static final long serialVersionUID = 204168291291342844L;
    private static final Dimension DIM = new Dimension(150, 100);


    FeedbackPanel() {
        setLayout(new BorderLayout());
        setName(GuiConfig.getInstance().getPnlFeedbackTitle());
        createComponents();
    }


    private void createComponents() {
        final JEditorPane txtEditorPane =
                TextComponentBuilder.buildEditorPane(GuiConfig.getInstance().getPnlFeedbackTxt(), this, false);
        final JScrollPane cmpTile =
                ContainerBuilder.buildScrollPane(txtEditorPane, null, getBackground(), null, 100, false, DIM);
        add(cmpTile, BorderLayout.CENTER);
    }


    @Override
    public void hyperlinkUpdate(final HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                OpenBrowser.displayUrl(new URI(Config.getInstance().getFeedbackUrl()));
            } catch (final Exception e) {
                JOptionPane.showMessageDialog(MainApplication.getMainFrame(),
                        tr(GuiConfig.getInstance().getTxtFeedbackUrlError()),
                        tr(GuiConfig.getInstance().getErrorTitle()), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}