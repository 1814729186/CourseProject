package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

import java.io.IOException;

/**
 * <pre>
 * 派生自AbstractTermTupleFilter的子类
 *      基于正则表达式的过滤器
 * </pre>
 */
public class PatternTermTupleFilter extends AbstractTermTupleFilter {
    /**
     * 静态常量参数
     */
    //public static final String REGEX = Config.STRING_SPLITTER_REGEX;

    /**
     * 构造函数
     * @param input Filter输入
     */
    public PatternTermTupleFilter(AbstractTermTupleStream input){super(input);}
    @Override
    /**
     * 获得下一个三元组
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    public AbstractTermTuple next()throws IOException {
        AbstractTermTuple termtuple = input.next();
        if(termtuple == null) return null;
        //丢弃不匹配项
        if(!termtuple.term.getContent().matches(Config.TERM_FILTER_PATTERN)) {return this.next();}
        else return termtuple;
    }

}
