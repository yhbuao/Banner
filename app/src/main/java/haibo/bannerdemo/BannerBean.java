package haibo.bannerdemo;

import java.util.List;

/**
 * @author: yuhaibo
 * @time: 2018/2/11 10:52.
 * projectName: Banner.
 * Description: Banner Model
 */

public class BannerBean {
    private List<BannersBean> banners;
    public List<BannersBean> getBanners() {
        return banners;
    }
    public void setBanners(List<BannersBean> banners) {
        this.banners = banners;
    }
    public static class BannersBean {
        private String schema_url;
        private BannerUrlBean banner_url;
        private List<?> target_users;

        public String getSchema_url() {
            return schema_url;
        }

        public void setSchema_url(String schema_url) {
            this.schema_url = schema_url;
        }

        public BannerUrlBean getBanner_url() {
            return banner_url;
        }

        public void setBanner_url(BannerUrlBean banner_url) {
            this.banner_url = banner_url;
        }

        public List<?> getTarget_users() {
            return target_users;
        }

        public void setTarget_users(List<?> target_users) {
            this.target_users = target_users;
        }

        public static class BannerUrlBean {
            private String title;
            private String uri;
            private int height;
            private int width;
            private int id;
            private List<UrlListBean> url_list;
            public String getTitle() {
                return title;
            }
            public void setTitle(String title) {
                this.title = title;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public List<UrlListBean> getUrl_list() {
                return url_list;
            }

            public void setUrl_list(List<UrlListBean> url_list) {
                this.url_list = url_list;
            }

            public static class UrlListBean {
                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }
    }
    //1
}
