package emad.youtube.Model.Video;

public class PageInfo {
    private int totalResults;
    private int resultsPerPage;

    public PageInfo() {
    }

    public PageInfo(int totalResults, int resultsPerPage) {
        this.totalResults = totalResults;
        this.resultsPerPage = resultsPerPage;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }
}
