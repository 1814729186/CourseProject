package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StopWords;

import java.io.IOException;
import java.util.Arrays;

/**
 * <pre>
 * 派生自AbstractTermTupleFilter的子类
 *      停用词过滤器
 * </pre>
 */
public class StopWordTermTupleFilter extends AbstractTermTupleFilter {
    /**
     * 构造函数
     * @param input Filter输入
     */
    public StopWordTermTupleFilter(AbstractTermTupleStream input){super(input);}
    /**
     * 获得下一个三元组
     * @return : 下一个三元组；如果到了流的末尾，返回null
     * @throws IOException : 抛出的IO异常
     */
    public AbstractTermTuple next()throws IOException {
        AbstractTermTuple termTuple = input.next();
        if (termTuple == null) { return null; }
        //在停用词表中，递归调用返回下一个
        if (Arrays.asList(StopWords.STOP_WORDS).contains(termTuple.term.getContent())) { return this.next(); }
        else return termTuple;
    }

}
