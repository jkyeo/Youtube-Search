package mobile.tap.youtubesearch.repo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author Tim Yeo
 * @desc Tips: Service for Search
 * @date 2021/10/17 20:22
 */
public class SearchService {

    private static final String API_URL = "https://www.youtube.com";

    // Singleton define start
    private SearchService() {}
    private Object readResolve() {
        return getInstance();
    }
    private static class InstanceHolder {
        static final SearchService instance = new SearchService();
    }
    public static SearchService getInstance() {
        return InstanceHolder.instance;
    }
    // Singleton define end

    // Create OkHttp
    private final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    // Create Retrofit
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build();

    public SearchApi createSearchService() {
        return retrofit.create(SearchApi.class);
    }
}
