package edu.stanford.webprotege.issues.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class NextPageExtractor {

    public Optional<Integer> extractNextPage(HttpHeaders headers) {
        var link = headers.getFirst("Link");
        if (link == null) {
            return Optional.empty();
        }
        var matcher = Pattern.compile("<[^>]*page=(\\d+)[^>]*>; rel=\"next\"").matcher(link);
        if (matcher.find()) {
            return Optional.of(Integer.parseInt(matcher.group(1)));
        }
        return Optional.empty();
    }
}
