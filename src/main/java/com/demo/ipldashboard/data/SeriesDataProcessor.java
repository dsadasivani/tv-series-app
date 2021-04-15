package com.demo.ipldashboard.data;

import com.demo.ipldashboard.model.Series;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class SeriesDataProcessor implements ItemProcessor<SeriesInput, Series> {

    private static final Logger log = LoggerFactory.getLogger(SeriesDataProcessor.class);

    @Override
    public Series process(final SeriesInput seriesInput) throws Exception {

        Series series = new Series();
        series.setSerialNumber(Long.parseLong(seriesInput.getSerial_number()));
        series.setPosterLink(seriesInput.getPoster_Link());
        series.setSeriesTitle(seriesInput.getSeries_Title());
        series.setRuntimeOfSeries(seriesInput.getRuntime_of_Series());
        series.setCertificate(seriesInput.getCertificate());
        series.setRuntimeOfEpisodes(seriesInput.getRuntime_of_Episodes());
        series.setGenre(seriesInput.getGenre());
        series.setImdbRating(Double.parseDouble(seriesInput.getIMDB_Rating()));
        series.setOverview(seriesInput.getOverview());
        series.setStar1(seriesInput.getStar1());
        series.setStar2(seriesInput.getStar2());
        series.setStar3(seriesInput.getStar3());
        series.setStar4(seriesInput.getStar4());
        series.setNoOfVotes(Long.parseLong(seriesInput.getNo_of_Votes()));

        return series;
    }
}
