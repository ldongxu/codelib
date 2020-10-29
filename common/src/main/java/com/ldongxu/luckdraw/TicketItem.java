package com.ldongxu.luckdraw;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author liudongxu06
 * @since 2020/10/27
 */
@ToString
@Data
@NoArgsConstructor
public class TicketItem<T> {
    private boolean isSupply;
    private T data;

    public TicketItem(T data) {
        this.data = data;
    }

}
