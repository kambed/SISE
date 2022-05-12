package frontend;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.*;

public class ChartGenerator {
    public static JFreeChart generatePlot(double[] x, double[] y) {
        XYSeries errorFunctionSeries = new XYSeries("Error function");
        for (int i = 0; i < x.length; i++) {
            errorFunctionSeries.add(x[i], y[i]);
        }

        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        seriesCollection.addSeries(errorFunctionSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Error function", "Eras", "Error", seriesCollection,
                PlotOrientation.VERTICAL, false, true, false
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        changeVisibility(renderer, 0, true);
        changeVisibility(renderer, 1, true);
        formatAxis(renderer, 2);
        formatAxis(renderer, 3);

        plot.setRenderer(renderer);

        return chart;
    }

    private static void formatAxis(XYLineAndShapeRenderer renderer, int series) {
        changeVisibility(renderer, series, true);
        renderer.setSeriesStroke(series, new BasicStroke(0.5f));
    }

    private static void changeVisibility(XYLineAndShapeRenderer renderer, int series, boolean displayLine) {
        renderer.setSeriesLinesVisible(series, displayLine);
        renderer.setSeriesShapesVisible(series, !displayLine);
    }
}