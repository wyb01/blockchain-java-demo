package org.zero01.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.zero01.core.BlockChain;

/**
 * @program: BlockChain_Java
 * @description: 该Servlet用于接收并处理新的交易信息
 * @author: wyb
 * @create: 2019-09-12 11:54
 **/
@WebServlet("/transactions/new")
public class NewTransaction extends HttpServlet {


	/** 
	* @Description:
	* @Param: [req, resp] 
	* @return: void 
	* @Author: wyb
	* @Date: 2019/9/12 11:52
	*/
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		// 读取客户端传递过来的数据并转换成JSON格式
		BufferedReader reader = req.getReader();
		String input = null;
		StringBuffer requestBody = new StringBuffer();
		while ((input = reader.readLine()) != null) {
			requestBody.append(input);
		}
		JSONObject jsonValues = new JSONObject(requestBody.toString());

		// 检查所需要的字段是否位于POST的data中
		String[] required = { "sender", "recipient", "amount" };
		for (String string : required) {
			if (!jsonValues.has(string)) { //检验传入数据格式是否合法
				// 如果没有需要的字段就返回错误信息
				resp.sendError(400, "Missing values");
			}
		}

		// 新建交易信息
		BlockChain blockChain = BlockChain.getInstance();  //创建单例对象
		int index = blockChain.newTransactions(jsonValues.getString("sender"), jsonValues.getString("recipient"),
				jsonValues.getLong("amount")); //向列表中添加一个交易记录，并返回该记录将被添加到的区块 (下一个待挖掘的区块)的索引

		// 返回json格式的数据给客户端
		resp.setContentType("application/json");
		PrintWriter printWriter = resp.getWriter();
		printWriter.println(new JSONObject().append("message", "Transaction will be added to Block " + index));
		printWriter.close();
	}
}
