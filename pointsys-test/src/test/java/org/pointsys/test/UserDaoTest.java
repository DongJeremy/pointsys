package org.pointsys.test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.collection.IsMapContaining;
import org.hamcrest.text.IsEqualCompressingWhiteSpace;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Before;
import org.junit.Test;

public class UserDaoTest {

    private User test1;
    private User test2;

    @Before
    public void init() {
        test1 = new User();
        test1.setUsername("tt1");
        test1.setPassword("123");
        test1.setShares(51);
        test2 = new User();
        test2.setUsername("tt2");
        test2.setPassword("321");
        test2.setShares(20);
    }

    @Test
    public void test() {
        /** 数值匹配 **/
        // 测试变量是否大于指定值
        assertThat(test1.getShares(), greaterThan(50));
        // 测试变量是否小于指定值
        assertThat(test1.getShares(), lessThan(100));
        // 测试变量是否大于等于指定值
        assertThat(test1.getShares(), greaterThanOrEqualTo(50));
        // 测试变量是否小于等于指定值
        assertThat(test1.getShares(), lessThanOrEqualTo(100));

        // 测试所有条件必须成立
        assertThat(test1.getShares(), allOf(greaterThan(50), lessThan(100)));
        // 测试只要有一个条件成立
        assertThat(test1.getShares(), anyOf(greaterThanOrEqualTo(50), lessThanOrEqualTo(100)));
        // 测试无论什么条件成立(还没明白这个到底是什么意思)
        assertThat(test1.getShares(), anything());
        // 测试变量值等于指定值
        assertThat(test1.getShares(), is(100));
        // 测试变量不等于指定值
        assertThat(test1.getShares(), not(50));

        /** 字符串匹配 **/
        String url = "http://www.taobao.com";
        // 测试变量是否包含指定字符
        assertThat(url, containsString("taobao"));
        // 测试变量是否已指定字符串开头
        assertThat(url, startsWith("http://"));
        // 测试变量是否以指定字符串结尾
        assertThat(url, endsWith(".com"));
        // 测试变量是否等于指定字符串
        assertThat(url, equalTo("http://www.taobao.com"));
        // 测试变量再忽略大小写的情况下是否等于指定字符串
        assertThat(url, IsEqualIgnoringCase.equalToIgnoringCase("http://www.taobao.com"));
        // 测试变量再忽略头尾任意空格的情况下是否等于指定字符串
        assertThat(url, IsEqualCompressingWhiteSpace.equalToCompressingWhiteSpace("http://www.taobao.com"));

        /** 集合匹配 **/

        List<User> user = new ArrayList<User>();
        user.add(test1);
        user.add(test2);

        // 测试集合中是否还有指定元素
        assertThat(user, hasItem(test1));
        assertThat(user, hasItem(test2));

        /** Map匹配 **/
        Map<String, User> userMap = new HashMap<String, User>();
        userMap.put(test1.getUsername(), test1);
        userMap.put(test2.getUsername(), test2);

        // 测试map中是否还有指定键值对
        assertThat(userMap, IsMapContaining.hasEntry(test1.getUsername(), test1));
        // 测试map中是否还有指定键
        assertThat(userMap, IsMapContaining.hasKey(test2.getUsername()));
        // 测试map中是否还有指定值
        assertThat(userMap, IsMapContaining.hasValue(test2));
    }

}
