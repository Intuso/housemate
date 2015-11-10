package com.intuso.housemate.extension.android.widget.configure;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import com.intuso.housemate.extension.android.widget.service.WidgetService;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 27/02/14
 * Time: 19:01
 * To change this template use File | Settings | File Templates.
 */
public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        WidgetService.deleteWidgets(context, appWidgetIds);
        super.onDeleted(context, appWidgetIds);
    }
}