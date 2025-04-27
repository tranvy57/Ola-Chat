/*
 * @ (#) PollResultsResponse.java       1.0     27/04/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.dtos.responses;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 27/04/2025
 * @version:    1.0
 */

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PollResultsResponse {
    private PollResponse poll;
    private List<PollOptionResult> options;
    private List<Voter> voters;
}
