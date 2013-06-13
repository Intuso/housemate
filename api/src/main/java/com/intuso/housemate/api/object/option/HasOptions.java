package com.intuso.housemate.api.object.option;

import com.intuso.housemate.api.object.list.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */
public interface HasOptions<L extends List<? extends Option<?>>> {
    public L getOptions();
}