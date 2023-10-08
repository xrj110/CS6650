package Client2;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.UIUtils;
import org.jfree.chart.ui.VerticalAlignment;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Plot {

    public static void generate(){
        PerformanceChart chart = new PerformanceChart("Performance Throughput Chart");
        chart.pack();
        UIUtils.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}
class PerformanceChart extends ApplicationFrame {
    public PerformanceChart(String title) {
        super(title);
        JFreeChart chart = createChart(createDataset());
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1200, 600));
        setContentPane(chartPanel);
    }

    private XYDataset createDataset() {
        TimeSeries goThroughputSeries = new TimeSeries("Go Throughput (req/sec)");
        TimeSeries javaThroughputSeries = new TimeSeries("Java Throughput (req/sec)");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        try (BufferedReader goReader = new BufferedReader(new FileReader("GoRecord.csv"));
             BufferedReader javaReader = new BufferedReader(new FileReader("JavaRecord.csv"))) {

            String goLine, javaLine;
            long goStart = 0, javaStart = 0;
            int goCount = 0, javaCount = 0;

            while ((goLine = goReader.readLine()) != null && (javaLine = javaReader.readLine()) != null) {
                String[] goParts = goLine.split(",");
                String[] javaParts = javaLine.split(",");

                if (goParts.length >= 2 && javaParts.length >= 2) {
                   //Go data
                    if (goStart == 0) {
                        goStart = Long.parseLong(goParts[0]);
                        goCount++;
                    } else if (goStart > 0 && Long.parseLong(goParts[0]) - goStart < 1000) {
                        goCount++;
                    } else if (goStart > 0 && Long.parseLong(goParts[0]) - goStart > 1000) {
                        double goThroughput = goCount;
                        goThroughputSeries.addOrUpdate(new Second(new Date(goStart)), goThroughput);
                        goStart = 0;
                        goCount = 0;
                    }

                    // Java data
                    if (javaStart == 0) {
                        javaStart = Long.parseLong(javaParts[0]);
                        javaCount++;
                    } else if (javaStart > 0 && Long.parseLong(javaParts[0]) - javaStart < 1000) {
                        javaCount++;
                    } else if (javaStart > 0 && Long.parseLong(javaParts[0]) - javaStart > 1000) {
                        double javaThroughput = javaCount;
                        javaThroughputSeries.addOrUpdate(new Second(new Date(javaStart)), javaThroughput);
                        javaStart = 0;
                        javaCount = 0;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(goThroughputSeries);
        dataset.addSeries(javaThroughputSeries);
        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Performance Throughput",
                "Time",
                "Throughput (req/sec)",
                dataset,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        XYSplineRenderer goRenderer = new XYSplineRenderer();
        XYSplineRenderer javaRenderer = new XYSplineRenderer();


        goRenderer.setSeriesPaint(0, Color.BLUE);
        javaRenderer.setSeriesPaint(1, Color.RED);

        plot.setRenderer(0, goRenderer);
        plot.setRenderer(1, javaRenderer);
        plot.setDomainPannable(true);
        plot.setRangePannable(true);

        chart.setTitle(new TextTitle("Performance Throughput", new Font("Helvetica", Font.BOLD, 24)));
        chart.setBorderVisible(true);
        chart.setBorderPaint(Color.BLACK);
        chart.setPadding(new RectangleInsets(10, 10, 10, 10));
        chart.setBackgroundPaint(Color.WHITE);

        plot.getDomainAxis().setVerticalTickLabels(true);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        return chart;
    }



}