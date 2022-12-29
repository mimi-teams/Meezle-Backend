package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.domain.type.SchedulerDataType;
import com.mimi.w2m.backend.dto.base.ApiCallResponse;
import com.mimi.w2m.backend.service.SchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * SchedulerApi: GCP 의 Scheduling 이용하기 위한 Api
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/12/29
 **/
@Tag(name = "Scheduler Api", description = "Backend Scheduling 과 관련된 Api. Client 에서는 무시해도 괜찮습니다.")
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/v1/schedulers")
@RestController
public class SchedulerApi {
    private final SchedulerService schedulerService;

    @Operation(summary = "만료된 정보 정리", description = "[인증X] 더이상 이용되지 않는 정보를 정리한다.")
    @PostMapping(path = "/clean")
    public @Valid ApiCallResponse<?> clean(
            @Parameter(name = "data", description = "정리할 데이터", in = ParameterIn.QUERY, required = true)
            @Valid @NotNull @RequestParam SchedulerDataType data
    ) {
        switch (data) {
            case LOGOUT_TOKEN -> schedulerService.cleanLogoutJwtTokens();
        }
        return ApiCallResponse.ofSuccess(null);
    }
}