package sontran.geocomply.homeassignment.comment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Comment {
    @SerializedName("mentions")
    private List<String> mentions;
    @SerializedName("links")
    private List<Link> links;

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    static class Link {
        @SerializedName("url")
        private String url;
        @SerializedName("title")
        private String title;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
