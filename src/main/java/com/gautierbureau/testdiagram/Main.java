/**
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.gautierbureau.testdiagram;

import com.powsybl.iidm.import_.Importers;
import com.powsybl.iidm.network.Network;
import com.powsybl.sld.NetworkGraphBuilder;
import com.powsybl.sld.SubstationDiagram;
import com.powsybl.sld.VoltageLevelDiagram;
import com.powsybl.sld.layout.*;
import com.powsybl.sld.library.ComponentLibrary;
import com.powsybl.sld.library.ResourcesComponentLibrary;
import com.powsybl.sld.svg.DefaultDiagramLabelProvider;
import com.powsybl.sld.svg.DefaultDiagramStyleProvider;
import com.powsybl.sld.svg.DefaultSVGWriter;
import com.powsybl.sld.util.NominalVoltageDiagramStyleProvider;

import java.nio.file.Paths;

/**
 * @author Mathieu Bague <mathieu.bague@rte-france.com>
 */
public class Main {

    public static void main(String[] args) {
        Network network = Importers.loadNetwork(args[0]);
        String type = args[1];
        String id = args[2];
        String outputFile = args[3];

        if (type.equals("VL")) {
            // "Convergence" style component library
            ComponentLibrary componentLibrary = new ResourcesComponentLibrary("/ConvergenceLibrary");

            // fully automatic layout
            VoltageLevelLayoutFactory voltageLevelLayoutFactory = new PositionVoltageLevelLayoutFactory(new PositionFromExtension());

            // create diagram for the voltage level "C"
            VoltageLevelDiagram voltageLevelDiagram = VoltageLevelDiagram.build(new NetworkGraphBuilder(network), id, voltageLevelLayoutFactory, false);

            // create default parameters for the SVG layout
            LayoutParameters layoutParameters = new LayoutParameters();

            // generate SVG
            voltageLevelDiagram.writeSvg("",
                    new DefaultSVGWriter(componentLibrary, layoutParameters),
                    new DefaultDiagramLabelProvider(network, componentLibrary, layoutParameters),
                    new DefaultDiagramStyleProvider(),
                    Paths.get(outputFile));
        } else if (type.equals("S")) {
            // "Convergence" style component library
            ComponentLibrary componentLibrary = new ResourcesComponentLibrary("/ConvergenceLibrary");

            // fully automatic layout
            VoltageLevelLayoutFactory voltageLevelLayoutFactory = new PositionVoltageLevelLayoutFactory(new PositionFromExtension());
            SubstationLayoutFactory substationLayoutFactory = new HorizontalSubstationLayoutFactory();

            // create diagram for the voltage level "C"
            SubstationDiagram substationDiagram = SubstationDiagram.build(new NetworkGraphBuilder(network), id, substationLayoutFactory, voltageLevelLayoutFactory, false);

            // create default parameters for the SVG layout
            LayoutParameters layoutParameters = new LayoutParameters();

            substationDiagram.writeSvg("",
                    new DefaultSVGWriter(componentLibrary, layoutParameters),
                    Paths.get(outputFile),
                    new DefaultDiagramLabelProvider(network, componentLibrary, layoutParameters),
                    new DefaultDiagramStyleProvider());
        }
    }
}
