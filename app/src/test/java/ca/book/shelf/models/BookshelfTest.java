package ca.book.shelf.models;

import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.*;

public class BookshelfTest {

    String sampleJson = "{\"stories\":" +
            "[" +
            "{\"id\":\"181470351\",\"title\":\"Welcum 2 h3ll 3\",\"user\":" +
            "{\"name\":\"Wafflezs\",\"avatar\":\"https:\\/\\/a.wattpad.com\\/useravatar\\/Wafflezs.128.994896.jpg\",\"fullname\":\"Cryptic Wafflezs\"}," +
            "\"cover\":\"https:\\/\\/a.wattpad.com\\/cover\\/181470351-256-k591667.jpg\"}" +
            "]," +
            "\"nextUrl\":\"https:\\/\\/www.wattpad.com\\/api\\/v3\\/stories?fields=stories%28id%2Ctitle%2Ccover%2Cuser%29&filter=new&limit=10&offset=10\"}";

    @Test
    public void parsing_test() {
        Gson gson = new Gson();
        Bookshelf result = gson.fromJson(sampleJson, Bookshelf.class);

        assertNotNull(result);
        assertNotNull(result.nextUrl);
    }
}
