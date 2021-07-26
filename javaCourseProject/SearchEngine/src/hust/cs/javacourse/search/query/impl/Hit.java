package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;

import java.util.Map;

/**
 * <pre>
 * Hit是一个搜索命中结果的抽象类. 该类要实现Comparable接口.
 * 实现该接口是因为需要必须比较大小，用于命中结果的排序.
 * </pre>
 */
public class Hit extends AbstractHit {
    /**
     * 缺省构造
     */
    public Hit(){}

    /**
     * 构造函数
     * @param docId 文档编号
     * @param docPath 文档路径
     */
    public Hit(int docId, String docPath){
        super(docId, docPath);
    }

    /**
     * 构造函数
     * @param docId 文档id
     * @param docPath 文档路径
     * @param termPostingMapping Mapping
     */
    public Hit(int docId, String docPath, Map<AbstractTerm, AbstractPosting> termPostingMapping){
        super(docId, docPath, termPostingMapping);
    }
    /**
     * 获得文档id
     * @return ： 文档id
     */
    public int getDocId(){return docId;}

    /**
     * 获得文档绝对路径
     * @return ：文档绝对路径
     */
    public String getDocPath(){return docPath;}

    /**
     * 获得文档内容
     * @return ： 文档内容
     */
    public String getContent(){return content;}

    /**
     * 设置文档内容
     * @param content ：文档内容
     */
    public void setContent(String content){
        this.content = content;
    }

    /**
     * 获得文档得分
     * @return ： 文档得分
     */
    public double getScore(){return score;}

    /**
     * 设置文档得分
     * @param score ：文档得分
     */
    public void setScore(double score){this.score = score;}

    /**
     * 获得命中的单词和对应的Posting键值对
     * @return ：命中的单词和对应的Posting键值对
     */
    public Map<AbstractTerm, AbstractPosting> getTermPostingMapping(){return termPostingMapping;}

    /**
     * 获得命中结果的字符串表示, 用于显示搜索结果.
     * @return : 命中结果的字符串表示
     */
    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("docId:"+docId+"\ndocPath:"+docPath+"\ncontent:"+content+"\nscore:"+score+"\n");
        //show map
        buffer.append("TermPostingMapping:\n");
        for(Map.Entry<AbstractTerm,AbstractPosting> entry : termPostingMapping.entrySet()){
            buffer.append(entry.getKey().getContent()+"\t---->\t"+entry.getValue()+"\n");
        }
        return buffer.toString();
    }

    /**
     * 比较二个命中结果的大小，根据score比较
     * @param o     ：要比较的名字结果
     * @return      ：二个命中结果得分的差值
     */
    @Override
    public int compareTo(AbstractHit o){return (int)(this.score - o.getScore());}
}
