package com.lth.antonlundborg.antonsprojekt.Charts;


import com.lth.antonlundborg.antonsprojekt.R;


public class BalticSeaPortalFragment extends ChartFragment {

    public BalticSeaPortalFragment() {}

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart;
    }

    @Override
    protected int getSpinnerArrayId() {
        return R.array.time_chart_array;
    }

    @Override
    public String[] getUrlNumbers() {
        return new String[]{"03", "06", "09", "12", "15", "18", "21", "24", "27", "30", "33", "36", "39",
                "42", "45", "48", "51", "54", "57", "60", "63", "66", "69", "72"};
    }

    @Override
    public String getUrlStart() {
        return "http://cdn.fmi.fi/marine-forecasts/products/wave-forecast-maps/wave";
    }

    @Override
    public String getUrlEnd() {
        return ".gif";
    }


}
