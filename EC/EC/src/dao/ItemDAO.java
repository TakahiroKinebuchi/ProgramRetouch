package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import base.DBManager;
import beans.ItemDataBeans;

public class ItemDAO {

	// インスタンスオブジェクトを返却させてコードの簡略化
	public static ItemDAO getInstance() {
		return new ItemDAO();
	}

	/**
	 * ランダムで引数指定分のItemDataBeansを取得
	 * @param limit 取得したいかず
	 * @return <ItemDataBeans>
	 * @throws SQLException
	 */
	public static ArrayList<ItemDataBeans> getRandItem(int limit) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();

			st = con.prepareStatement("SELECT * FROM m_item ORDER BY RAND() LIMIT ? ");
			st.setInt(1, limit);

			ResultSet rs = st.executeQuery();

			ArrayList<ItemDataBeans> itemList = new ArrayList<ItemDataBeans>();

			while (rs.next()) {
				ItemDataBeans item = new ItemDataBeans();
				item.setId(rs.getInt("id"));
				item.setName(rs.getString("name"));
				item.setDetail(rs.getString("detail"));
				item.setPrice(rs.getInt("price"));
				item.setFileName(rs.getString("file_name"));
				itemList.add(item);
			}
			System.out.println("getAllItem completed");
			return itemList;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	/**
	 * 商品IDによる商品検索
	 * @param itemId
	 * @return ItemDataBeans
	 * @throws SQLException
	 */
	public static ItemDataBeans getItemByItemID(int itemId) throws SQLException {
		Connection con = null;
		try {
			//DBに接続
			con = DBManager.getConnection();
			//SELECT文を準備
			String sql = "SELECT * FROM m_item WHERE id = ?";
			//SELECTを実行し、結果表を取得
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, itemId);
			ResultSet rs = st.executeQuery();

			//return文の準備として、ItemDataBeansのインスタンスを用意
			ItemDataBeans item = new ItemDataBeans();
			//主キーに紐づくレコードは1件だけなので、rs.next()は1回だけ行う
			if (!rs.next()) {
				return null;
			}

			//itemの各アクセサに取得したid,name,detail,price,file_nameをセットする
			item.setId(rs.getInt("id"));
			item.setName(rs.getString("name"));
			item.setDetail(rs.getString("detail"));
			item.setPrice(rs.getInt("price"));
			item.setFileName(rs.getString("file_name"));
			//コンソールに、引数でitemを取得完了したことを出力する
			System.out.println("searching item by itemID has been completed");

			return item;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	/**
	 * 商品検索
	 * @param searchWord
	 * @param pageNum
	 * @param pageMaxItemCount
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<ItemDataBeans> getItemsByItemName(String searchWord, int pageNum, int pageMaxItemCount) throws SQLException {
		Connection con = null;
		ArrayList<ItemDataBeans> itemList = new ArrayList<ItemDataBeans>();
		try {
			//DBに接続
			con = DBManager.getConnection();
			//表示開始インデックスを宣言、初期化
			int startiItemNum = (pageNum - 1) * pageMaxItemCount;

			//SELECT文を準備
			String sql = "SELECT * FROM m_item";


			if (!searchWord.equals("")) {
				//商品名検索の場合
				sql += " WHERE name LIKE '%" + searchWord + "%' ORDER BY id ASC LIMIT " + startiItemNum + "," + pageMaxItemCount;
			}else {
				//全検索の場合
				sql += " ORDER BY id ASC LIMIT " + startiItemNum + "," + pageMaxItemCount;
			}


			//Statementを宣言
			Statement st = con.createStatement();
			//SELECTを実行し、結果表を取得
			ResultSet rs = st.executeQuery(sql);
			System.out.println(sql);

			System.out.println(rs);
			while (rs.next()) {
				ItemDataBeans item = new ItemDataBeans();

				item.setId(rs.getInt("id"));
				item.setName(rs.getString("name"));
				item.setDetail(rs.getString("detail"));
				item.setPrice(rs.getInt("price"));
				item.setFileName(rs.getString("file_name"));

				itemList.add(item);
			}
			System.out.println("get Items by itemName has been completed");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
		return itemList;
	}
	/**
	 * 商品総数を取得
	 *
	 * @param searchWord
	 * @return
	 * @throws SQLException
	 */
	public static double getItemCount(String searchWord) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();
			st = con.prepareStatement("select count(*) as cnt from m_item where name like ?");
			st.setString(1, "%" + searchWord + "%");
			ResultSet rs = st.executeQuery();
			double coung = 0.0;
			while (rs.next()) {
				coung = Double.parseDouble(rs.getString("cnt"));
			}
			return coung;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

}
