package cn.tech.wings.cloud.gateway.handler;

import cn.tech.wings.cloud.gateway.license.AbstractServerInfos;
import cn.tech.wings.cloud.gateway.license.LinuxServerInfos;
import cn.tech.wings.cloud.gateway.license.WindowsServerInfos;
import cn.tech.wings.cloud.common.core.util.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class LicenseHandler implements HandlerFunction<ServerResponse> {

	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public Mono<ServerResponse> handle(ServerRequest serverRequest) {
		//操作系统类型
		String osName = System.getProperty("os.name").toLowerCase();
		AbstractServerInfos abstractServerInfos = null;
		//根据不同操作系统类型选择不同的数据获取方法
		if (osName.startsWith("windows")) {
			abstractServerInfos = new WindowsServerInfos();
		} else if (osName.startsWith("linux")) {
			abstractServerInfos = new LinuxServerInfos();
		} else {//其他服务器类型
			abstractServerInfos = new LinuxServerInfos();
		}
		return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(objectMapper.writeValueAsString(R.ok(abstractServerInfos.getServerInfos()))));
	}

}
