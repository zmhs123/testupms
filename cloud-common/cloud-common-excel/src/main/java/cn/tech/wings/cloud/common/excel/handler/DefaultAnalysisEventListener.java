package cn.tech.wings.cloud.common.excel.handler;

import cn.tech.wings.cloud.common.excel.annotation.ExcelLine;
import cn.tech.wings.cloud.common.excel.vo.ErrorMessage;
import com.alibaba.excel.context.AnalysisContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认的 AnalysisEventListener
 *
 * @author cloud
 * @author L.cm
 * @date 2021/4/16
 */
@Slf4j
public class DefaultAnalysisEventListener extends ListAnalysisEventListener<Object> {

	private final List<Object> list = new ArrayList<>();

	private final List<ErrorMessage> errorMessageList = new ArrayList<>();

	private Long lineNum = 1L;

	@Override
	public void invoke(Object o, AnalysisContext analysisContext) {
		lineNum++;

//		Set<ConstraintViolation<Object>> violations = Validators.validate(o);
//		if (!violations.isEmpty()) {
//			Set<String> messageSet = violations.stream().map(ConstraintViolation::getMessage)
//					.collect(Collectors.toSet());
//			errorMessageList.add(new ErrorMessage(lineNum, messageSet));
//		}
//		else {
			Field[] fields = o.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(ExcelLine.class) && field.getType() == Long.class) {
					try {
						field.setAccessible(true);
						field.set(o, lineNum);
					}
					catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			list.add(o);
//		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		log.debug("Excel read analysed");
	}

	@Override
	public List<Object> getList() {
		return list;
	}

	@Override
	public List<ErrorMessage> getErrors() {
		return errorMessageList;
	}

}
