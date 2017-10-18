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

    public JDBC() throws Exception {
        try {
            //-----------------
            // 接続
            //-----------------
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/adelaidedb", // "jdbc:postgresql://[場所(Domain)]:[ポート番号]/[DB名]"
                    "postgres", // ログインロール
                    "yusuke"); // パスワード
            statement = connection.createStatement();
        } catch (Exception e) {
            System.out.println("couldn't establish connection.");
        }
    }

    public List<String> ExecSql(String query) throws Exception {
        List<String> fields = new ArrayList<String>();
        try {
            //-----------------
            // SQLの発行
            //-----------------
            //ユーザー情報のテーブル
            resultSet = statement.executeQuery("select * from detail");

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
            System.out.println("couldn't exec sql.");
        }
        return fields;
    }

    public void Deserialize(List<String> fields) throws Exception {
        try {
            //結果の出力
            int rowCount = 0;
            while (resultSet.next()) {
                rowCount++;

                System.out.println("---------------------------------------------------");
                System.out.println("--- Rows:" + rowCount);
                System.out.println("---------------------------------------------------");

                //値は、「resultSet.getString(<フィールド名>)」で取得する。
                for (String field : fields) {
                    System.out.println(field + ":" + resultSet.getString(field));
                }
            }
        } catch (SQLException e) {
            System.out.println("couldn't show result");
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
