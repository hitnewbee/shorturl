package models;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints={ @UniqueConstraint(columnNames={"keyword"})})
public class links {

    @Id
    @SequenceGenerator(name = "fordemo",sequenceName = "t_links_seq",initialValue = 100,allocationSize = 10)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "fordemo")
    public long id;

    public String url;

    public String keyword;

    public String type;

    public long clickcount;

    public String insert_at;

    public long getClickcount() {
        return clickcount;
    }

    public void setClickcount(long clickcount) {
        this.clickcount = clickcount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInsert_at() {
        return insert_at;
    }

    public void setInsert_at(String insert_at) {
        this.insert_at = insert_at;
    }

}
