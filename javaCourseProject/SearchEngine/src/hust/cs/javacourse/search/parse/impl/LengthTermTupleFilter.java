package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

import java.io.IOException;

/**
 * <pre>
 * 派生自AbstractTermTupleFilter的子类
 *      基于单词长度的过滤器，相较于父类，添加单词长度限制实例成员变量
 *          maxLength : 过滤器基于的单词长度
 *
 * </pre>
 */
public class LengthTermTupleFilter extends AbstractTermTupleFilter {

    /**
     * 过滤器使用的单词限长
     */
    //public static final int maxLength = Config.TERM_FILTER_MAXLENGTH;
    //public static final int minLength = Config.TERM_FILTER_MINLENGTH;

    /**
     * 构造函数
     * @param input Filter输入
     */
    public LengthTermTupleFilter(AbstractTermTupleStream input){super(input);}

    @Override
    /**
     * 获得下一个三元组
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    public AbstractTermTuple next()throws IOException {
        AbstractTermTuple termtuple = input.next();
        if(termtuple == null) return null;
        if(termtuple.term.getContent().length() >= Config.TERM_FILTER_MINLENGTH && termtuple.term.getContent().length()<=Config.TERM_FILTER_MAXLENGTH){
            return termtuple;
        }else{
            //调用当前类的next方法，递归调用返回下一个
            return this.next();
        }
    }


}
