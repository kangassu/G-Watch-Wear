/*
 * Copyright (C) 2021 Juraj Antal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sk.trupici.gwatch.wear.config.complications;

/**
 * Data for Complications Preview item in RecyclerView.
 */
public class ComplicationConfigItem implements ConfigItem {

    final private int defaultComplicationResourceId;
    final private ComplicationLocation location;
    final private int layoutId;
    final private int complicationLayoutId;

    public ComplicationConfigItem(ComplicationLocation location, int defaultComplicationResourceId, int layoutId, int complicationLayoutId) {
        this.defaultComplicationResourceId = defaultComplicationResourceId;
        this.location = location;
        this.layoutId = layoutId;
        this.complicationLayoutId = complicationLayoutId;
    }

    public int getDefaultComplicationResourceId() {
        return defaultComplicationResourceId;
    }

    public ComplicationLocation getLocation() {
        return location;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getComplicationLayoutId() {
        return complicationLayoutId;
    }

    @Override
    public Type getConfigType() {
        return Type.TYPE_COMPLICATION;
    }
}
