package com.jasonfunderburker.couchpotato;

import com.jasonfunderburker.couchpotato.service.check.type.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Created on 21.03.2017
 *
 * @author JasonFunderburker
 */
@Configuration
public class TypeRetrieverConfig {

    @Bean
    public TorrentRetriever lostFilmTypeRetriever() {
        return new LostFilmTypeRetriever();
    }

    @Bean
    public TorrentRetriever nnmClubTypeRetriever() {
        return new NNMClubTypeRetriever();
    }

    @Bean
    public TorrentRetriever rutrackerTypeRetriever() {
        return new RutrackerTypeRetriever();
    }

    @Bean
    public TorrentRetriever newStudioTypeRetriever() {
        return new NewStudioTypeRetriever();
    }

    @Bean
    public TorrentRetriever voProductionTypeRetriever() {
        return new VoProductionTypeRetriever();
    }

    @Bean
    public StateRetrieversDictionary retrieversDictionary() {
        return new StateRetrieversDictionary(Arrays.asList(lostFilmTypeRetriever(), nnmClubTypeRetriever(),
                rutrackerTypeRetriever(), newStudioTypeRetriever(), voProductionTypeRetriever()));
    }

}
