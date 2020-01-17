package bin.framework;

import bin.framework.exception.BinMultiDataSourceException;

/**
 * 表名查找器
 */
public class TableNameFinder {

    /**
     * 根据给定的sql脚本解析出sql脚本对应的表名,封装到GiEn对象。
     * <p>只会在SQL中从0开始查找出第一个出现的表名。</p>
     * @param sql sql脚本
     * @return 查找对应连接配置的依据 封装类对象
     */
    public GiEn findGiTn(String sql){
        String upperSql = sql.trim().toUpperCase();
        GiEn giEn =null;
        if(upperSql.startsWith(SqlCommand.SELECT.toString())){
            giEn =findTableNameBySelect(upperSql);
            giEn.setSqlCommand(SqlCommand.SELECT);
        }else if(upperSql.startsWith(SqlCommand.INSERT.toString())){
            giEn =findTableNameByInsert(upperSql);
            giEn.setSqlCommand(SqlCommand.INSERT);
        }else if(upperSql.startsWith(SqlCommand.UPDATE.toString())){
            giEn =findTableNameByUpdate(upperSql);
            giEn.setSqlCommand(SqlCommand.UPDATE);
        }else if(upperSql.startsWith(SqlCommand.DELETE.toString())){
            giEn =findTableNameByDelete(upperSql);
            giEn.setSqlCommand(SqlCommand.DELETE);
        }else if(upperSql.startsWith(SqlCommand.CALL.toString())){
            giEn =findTableNameByProcedure(upperSql);
            giEn.setProcedure(true);
            giEn.setSqlCommand(SqlCommand.CALL);
        }
        return giEn;
    }

    private GiEn findTableNameByProcedure(String upperSql){
        return findTableNameByOffsetKey(upperSql,"CALL ");
    }

    private GiEn findTableNameByDelete(String upperSql){
        return findTableNameByOffsetKey(upperSql," FROM ");
    }

    private GiEn findTableNameByUpdate(String upperSql){
        return findTableNameByOffsetKey(upperSql,"UPDATE ");
    }

    private GiEn findTableNameByInsert(String upperSql){
        return findTableNameByOffsetKey(upperSql," INTO ");
    }

    private GiEn findTableNameBySelect(String upperSql) {
        return findTableNameByOffsetKey(upperSql, " FROM ");
    }

    private GiEn findTableNameByOffsetKey(String upperSql, String offsetKey) {
        int fromKeyIndex = upperSql.indexOf(offsetKey);
        if(fromKeyIndex==-1){
            throw new BinMultiDataSourceException(" no found ' FROM ' in sql : "+upperSql);
        }
        int formKeyEndIndex=fromKeyIndex+offsetKey.length();
        char[] upperSqlCharArr = upperSql.toCharArray();
        int tableNamelength=0;
        for (int i = formKeyEndIndex; i < upperSql.length(); i++) {
            char c = upperSqlCharArr[i];
            tableNamelength++;
            if(c==' ' && tableNamelength>0) break;
        }
        String gitnExpression = upperSql.substring(formKeyEndIndex, formKeyEndIndex + tableNamelength);
        return new GiEn(gitnExpression.trim());
    }


}
