package crawler;

import java.util.List;

import edu.uci.ics.crawler4j.fetcher.PageFetchResult;

/**
 * 分析器根类，被不同类型的分析器继承，实现不同类型需求的分析操作
 */
public class RootAnalyzer implements Runnable {

    /**
     * 只分析指定host下的url(由外部注入)
     */
    protected String seedHost;

    public void setSeedHost(String seedHost) {
        this.seedHost = seedHost;
    }

    /**
     * 任务开始入口（需要被子类覆盖）
     */
    @Override
    public void run() {
        while (!isTaskFinished()) {
            // 大致的处理过程：
            List<PageFetchResult> pages = this.getPageFetchResults(50);// 1.获取待处理页面信息
            for (PageFetchResult pageFetchResult : pages) {
                try {
                    AnalyzeResult rs = this.analyze(pageFetchResult);// 2. 执行分析方法（不同类型的分析器有不同的分析逻辑）
                    if (rs != null) {
                        this.saveAnalyzeResult(rs);// 保存分析结果
                    }
                } catch (Exception e) {
                    // 异常处理
                }
            }
        }
    }

    /**
     * 判断是否结束任务的条件 1.result_page表中已经没有未分析的host=seedHost的url了
     */
    protected Boolean isTaskFinished() {
        // TODO
        return false;
    }

    private void saveAnalyzeResult(AnalyzeResult rs) {
        // TODO Auto-generated method stub

    }

    private AnalyzeResult analyze(PageFetchResult pageFetchResult) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<PageFetchResult> getPageFetchResults(int max) {
        // TODO Auto-generated method stub
        return null;
    }

}
