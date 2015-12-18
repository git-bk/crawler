package crawler;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import edu.uci.ics.crawler4j.fetcher.PageFetchResult;

/**
 * 抓取器根类，被不同类型的抓取器继承，实现不同类型需求的抓取操作
 */
public class RootCrawler implements Runnable {

    /**
     * 只爬行指定host下的url(由外部注入)
     */
    protected String seedHost;

    public void setSeedHost(String seedHost) {
        this.seedHost = seedHost;
    }

    /**
     * 用指定的链接池进行页面内容获取(由外部注入)
     */
    protected HttpClient httpClient;

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * http连接最小延迟间隔(由外部注入)
     */
    protected long politenessDelay;

    public void setPolitenessDelay(long politenessDelay) {
        this.politenessDelay = politenessDelay;
    }

    /**
     * 该类维护一个待处理url的Map(key:host,value:url>，可以批量添加、取出指定host的url列表(由外部注入)
     */
    protected CrawlQueue crawlQueue;

    public void setCrawlQueue(CrawlQueue crawlQueue) {
        this.crawlQueue = crawlQueue;
    }

    /**
     * 一个互斥锁
     */
    protected Object mutex;

    public void setMutex(Object mutex) {
        this.mutex = mutex;
    }

    /**
     * key:网站域名 ,value：lastFetchTime
     */
    protected Map<String, Long> timeStamp;

    private List<String>        urls;

    /**
     * 任务开始入口（需要被子类覆盖）
     */
    @Override
    public void run() {
        while (!isTaskFinished()) {
            // 大致的处理过程：
            List<String> urls = getUrls(50);// 1.获取待处理url，最多获取50条，可能没有50条。
            for (String toFetchURL : urls) {
                try {
                    PageFetchResult rs = this.fetchPage(toFetchURL);// 2. 通过httpClient获取页面信息（里面有延时逻辑）
                    // 3.判断拿到的页面结果是否符合爬行结果特征（方法里面应该有过滤是否符合结果页面特征的过滤逻辑），如果是，将结果页面信息持久化到数据库中。
                    this.saveFetchResult(rs);
                    // 4.获取页面中的待处理urls（方法里面应该有过滤待处理urls页面的逻辑：筛选需要继续爬行的urls）
                    List<String> fetchUrls = this.getValidSubUrls(rs);
                    // 5. 将4中获得的待处理urls放到待处理url map中。
                    this.addUrls(fetchUrls);
                } catch (Exception e) {
                    // 异常处理
                }
            }
        }
    }

    /**
     * 判断拿到的页面结果是否符合爬行结果特征，如果是，将结果页面信息持久化到数据库中。
     * 
     * @param rs
     */
    protected void saveFetchResult(PageFetchResult rs) {
        // TODO
    }

    /**
     * @param rs
     * @return
     */
    protected List<String> getValidSubUrls(PageFetchResult rs) {
        // TODO
        return null;
    }

    /**
     * 判断是否结束任务的条件 1.待处理队列中已经没有host=seedHost的url了 ；或者 2.配置了最大fetch页面数，且已经到达这个上限。
     */
    protected Boolean isTaskFinished() {
        // TODO
        return false;
    }

    /**
     * 放入公共的httpclient链接池获取页面信息
     */
    protected PageFetchResult fetchPage(String toFetchURL) throws InterruptedException, ClientProtocolException,
                                                          IOException {
        HttpGet get = null;
        PageFetchResult fetchResult = new PageFetchResult();
        try {
            get = new HttpGet(toFetchURL);
            // 延时逻辑（同一个host的连接，限制最小间隔时间）
            synchronized (mutex) {
                long now = (new Date()).getTime();
                long lastFetchTime = timeStamp.get(seedHost);
                if (now - lastFetchTime < politenessDelay) {
                    Thread.sleep(politenessDelay - (now - lastFetchTime));// 等待时间到达最小间隔时间
                }
                timeStamp.put(seedHost, (new Date()).getTime());
            }

            HttpResponse response = httpClient.execute(get);
            fetchResult.setEntity(response.getEntity());
            fetchResult.setResponseHeaders(response.getAllHeaders());
            return fetchResult;
        } finally {
            if (fetchResult.getEntity() == null && get != null) {
                get.abort();
            }
        }
    }

    /**
     * 子类必须调用该方法来获取队列中的待处理url
     * 
     * @param max
     * @return
     */
    protected List<String> getUrls(int max) {
        return crawlQueue.getUrls(seedHost, max);
    }

    /**
     * 子类必须调用该方法来添加新的待处理url到队列中
     * 
     * @param max
     * @return
     */
    protected void addUrls(List<String> urls) {
        crawlQueue.addUrls(seedHost, urls);
    }
}
