# MySearch
索引模块核心方法initialize的代码：  

private void initialize() throws Exception {  

Directory returnIndexDir = MMapDirectory.open(new File(indexPath)，NoLockFactory.getNoLockFactory());  

Analyzer analyzer = new MaxWordAnalyzer();  

//分词器为MMSEG4J分词器，最大分词模式  

IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_48，analyzer); //设置分词器  

iwc.setSimilarity(new BM25Similarity());//设置使用Bm25排序算法  

IndexWriter writer = new IndexWriter(returnIndexDir， iwc);  

//使用以上配置创建索引器  

}  

JSP+Spring+DBUtils+BoneCP+DWR，框架简要介绍如下：  

1.Spring是近年来Java最流行的Web框架，其SpringMVC基于Servlet开发，效率较高，也全面支持注解，甚至可做到零配置文件。由于零配置相对复杂，全部写入Java代码中，可读性差。本系统采用注解与配置文件结合的方法，既减少了配置文件，又具有一定的可读性。  

2.DBUtils是个JDBC轻量级封装，简化了JDBC操作。其可以直接将查询出来的结果集封装成各种集合以及JavaBean，还能引入数据源、数据库连接池增强性能。  

3.BoneCP 是基于Java实现的开源数据库连接池实现库。BoneCP使用Jdk1.5的Concurrent包中的锁机制，还运用分区机制，性能较其他连接池有较大优势。BoneCP很小，只有四十几K，而相比之下C3P0要六百多K。  

4.DWR（Direct Web Remoting）是一个开源的Ajax开源框架。相较于其余框架，最大的特性就是可以暴露某些特定方法，浏览器端用JavaScript可直接调用。另外Dwr能够很好的与spring集成，直接调用spring中注入的bean。
