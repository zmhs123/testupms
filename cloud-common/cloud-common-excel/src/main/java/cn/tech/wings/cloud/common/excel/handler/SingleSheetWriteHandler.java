package cn.tech.wings.cloud.common.excel.handler;

import cn.tech.wings.cloud.common.excel.annotation.ResponseExcel;
import cn.tech.wings.cloud.common.excel.config.ExcelConfigProperties;
import cn.tech.wings.cloud.common.excel.enhance.WriterBuilderEnhancer;
import cn.tech.wings.cloud.common.excel.kit.ExcelException;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author cloud
 * @author L.cm
 * @date 2020/3/29
 * <p>
 * 处理单sheet 页面
 */
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

	public SingleSheetWriteHandler(ExcelConfigProperties configProperties,
                                   ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer excelWriterBuilderEnhance) {
		super(configProperties, converterProvider, excelWriterBuilderEnhance);
	}

	/**
	 * obj 是List 且list不为空同时list中的元素不是是List 才返回true
	 * @param obj 返回对象
	 * @return boolean
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List<?> objList = (List<?>) obj;
			return !objList.isEmpty() && !(objList.get(0) instanceof List);
		}
		else {
			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
		}
	}

	@Override
	public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
		List<?> list = (List<?>) obj;
		ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

		// 有模板则不指定sheet名
		Class<?> dataClass = list.get(0).getClass();
		WriteSheet sheet = this.sheet(responseExcel.sheets()[0], dataClass, responseExcel.template(),
				responseExcel.headGenerator());

		// 填充 sheet
		if (responseExcel.fill()) {
			excelWriter.fill(list, sheet);
		}
		else {
			// 写入sheet
			excelWriter.write(list, sheet);
		}
		excelWriter.finish();
	}

}
