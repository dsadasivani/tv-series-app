package com.demo.ipldashboard.data;

import javax.sql.DataSource;

import com.demo.ipldashboard.model.Series;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final String[] LIST_OF_FIELDS = new String[] { "serial_number", "Poster_Link", "Series_Title", "Runtime_of_Series",
            "Certificate", "Runtime_of_Episodes", "Genre", "IMDB_Rating", "Overview", "Star1", "Star2", "Star3",
            "Star4", "No_of_Votes" };

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<SeriesInput> reader() {
        return new FlatFileItemReaderBuilder<SeriesInput>().name("SeriesItemReader")
                .resource(new ClassPathResource("tv_series_data.csv")).delimited().names(LIST_OF_FIELDS)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<SeriesInput>() {
                    {
                        setTargetType(SeriesInput.class);
                    }
                }).build();
    }

    @Bean
    public SeriesDataProcessor processor() {
        return new SeriesDataProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Series> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Series>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO series (serial_number, poster_link, series_title, runtime_of_series, certificate, runtime_of_episodes, genre, imdb_rating, overview, star1, star2, star3, star4, no_of_votes)"
                        + " VALUES (:serialNumber, :posterLink, :seriesTitle, :runtimeOfSeries, :certificate, :runtimeOfEpisodes, :genre, :imdbRating, :overview, :star1, :star2, :star3, :star4, :noOfVotes)")
                .dataSource(dataSource).build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).listener(listener).flow(step1)
                .end().build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Series> writer) {
        return stepBuilderFactory.get("step1").<SeriesInput, Series>chunk(10).reader(reader()).processor(processor())
                .writer(writer).build();
    }
}
