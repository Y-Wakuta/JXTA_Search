package H_Bidirectional_Pipe_Communication;
// To change this license header, choose License Headers in Project Properties.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBC {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    public JDBC(String ip,String port,String dbName) throws Exception {
        try {
            //-----------------
            // 接続
            //-----------------
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://"+ip+":"+port+"/"+dbName,  // "jdbc:postgresql://[場所(Domain)]:[ポート番号]/[DB名]"
                    "postgres", // ログインロール
                    "yusuke"); // パスワード
            statement = connection.createStatement();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> ExecSql(String query) throws Exception {
        List<String> fields = new ArrayList<String>();
        try {
            //-----------------
            // SQLの発行
            //-----------------
            //ユーザー情報のテーブル
            resultSet = statement.executeQuery(query);

            //-----------------
            // 値の取得
            //-----------------
            // フィールド一覧を取得
            fields = new ArrayList<>();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                fields.add(rsmd.getColumnName(i));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return fields;
    }

    public String Deserialize(List<String> fields) throws Exception {
        String queryResult = "";
        try {
            //結果の出力
            while (resultSet.next()) {
                //値は、「resultSet.getString(<フィールド名>)」で取得する。
                for (String field : fields) {
                   queryResult +=field + ":" + resultSet.getString(field) +"\n";
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally{
            return queryResult;
        }
    }

    public void Close() {
        try {
            //接続を切断する
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
