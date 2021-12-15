/*
 * Copyright (C) 2019 Juraj Antal
 *
 * Originally created in G-Watch App
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

package sk.trupici.gwatch.wear.data;

import android.content.Context;

import sk.trupici.gwatch.wear.R;
import sk.trupici.gwatch.wear.util.UiUtils;

/**
 * Packet from AndroidAPS app containing
 * glucose value, IOB, COB, TBR, etc...
 */
public class AAPSPacket extends GlucosePacketBase {
    private static final String SOURCE_NAME = "AAPS";
    public static final int PACKET_DATA_SIZE = 43; // + N1 + N2 + N3

    private Double iob;
    private Double iobBolus;
    private Double iobBasal;

    private Double cob;
    private Double cobFuture;

    private Long basalTimestamp;
    private String basalProfile;
    private String tempBasalString;

    private Long pumpTimestamp;
    private Integer pumpBattery;
    private Double pumpReservoir;
    private String pumpStatus;

    public AAPSPacket(short bgValue, long bgTimestamp) {
        super(PacketType.AAPS, SOURCE_NAME, bgValue, bgTimestamp);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Packet implementation

    /*
     * AAPS PACKET
     *
     * [0] type
     * [1] 8-bit packet data len
     * [2] 32-bit timestamp
     * [6] 16-bit glucose value
     * [8] 32-bit glucose timestamp
     * [12] 32-bit cob
     * [16] 32-bit future carbs
     * [20] 32-bit iob
     * [24] 32-bit bolus iob
     * [28] 32-bit basal iob
     * [32] 32-bit basal timestamp
     * [36] 32-bit pump timestamp
     * [40] 16-bit pump reservoir
     * [42] 8-bit pump battery percent
     * [43] 8-bit basalProfile len (N1)
     * [44] N1 bytes of basalProfile string
     * [44+N1] 8-bit tempBasalString len (N2)
     * [45+N1] N2 bytes of basalProfile string
     * [45+N1+N2] 8-bit pumpStatus len (N3)
     * [46+N1+N2] N3 bytes of pumpStatus string
     */
    @Override
    public byte[] getData() {
        Integer dataSize = PACKET_DATA_SIZE
                + 1 + getNullableStrLen(basalProfile)
                + 1 + getNullableStrLen(tempBasalString)
                + 1 + getNullableStrLen(pumpStatus);
        byte[] data = new byte[PACKET_HEADER_SIZE + dataSize];
        int idx = 0;

        data[idx++] = getType().getCodeAsByte();
        data[idx++] = dataSize.byteValue();

        idx += encodeInt(data, idx, receivedAt / 1000); // time in seconds

        idx += encodeShort(data, idx, glucoseValue);
        idx += encodeInt(data, idx, timestamp / 1000); // time in seconds

        idx += encodeFloat(data, idx, (cob == null ? 0f : cob.floatValue()));
        idx += encodeFloat(data, idx, (cobFuture == null ? 0f : cobFuture.floatValue()));

        idx += encodeFloat(data, idx, (iob == null ? 0f : iob.floatValue()));
        idx += encodeFloat(data, idx, (iobBolus == null ? 0f : iobBolus.floatValue()));
        idx += encodeFloat(data, idx, (iobBasal == null ? 0f : iobBasal.floatValue()));

        idx += encodeInt(data, idx, (basalTimestamp == null || basalTimestamp < 0 ? 0L : basalTimestamp/1000));
        idx += encodeInt(data, idx, (pumpTimestamp == null || pumpTimestamp < 0 ? 0L : pumpTimestamp/1000));
        idx += encodeShort(data, idx, (pumpReservoir == null ? 0 : pumpReservoir.shortValue()));
        data[idx++] = pumpBattery == null ? 0 : pumpBattery.byteValue();

        idx += encodeString(data, idx, basalProfile);
        idx += encodeString(data, idx, tempBasalString);
        idx += encodeString(data, idx, pumpStatus);

        return data;
    }

    @Override
    public String toText(Context context, String header) {
        StringBuffer text = new StringBuffer(super.toText(context, header));

        text.append(context.getString(R.string.aaps_packet_cob, UiUtils.formatDoubleOrNoData(cob))).append("\n");
        text.append(context.getString(R.string.aaps_packet_iob, UiUtils.formatDoubleOrNoData(iob))).append("\n");
        text.append(context.getString(R.string.aaps_packet_profile, UiUtils.getStringOrNoData(basalProfile))).append("\n");
        text.append(context.getString(R.string.aaps_packet_tbr, UiUtils.getStringOrNoData(tempBasalString))).append("\n");
        text.append(context.getString(R.string.aaps_packet_pump, UiUtils.getStringOrNoData(pumpStatus))).append("\n");
        return text.toString();
    }

    ///////////////////////////////////////////////////////////////////////////

    public void setIob(Double iob) {
        this.iob = iob;
    }

    public void setIobBolus(Double iobBolus) {
        this.iobBolus = iobBolus;
    }

    public void setIobBasal(Double iobBasal) {
        this.iobBasal = iobBasal;
    }

    public void setCob(Double cob) {
        this.cob = cob;
    }

    public void setCobFuture(Double cobFuture) {
        this.cobFuture = cobFuture;
    }

    public void setBasalProfile(String basalProfile) {
        this.basalProfile = basalProfile;
    }

    public void setBasalTimestamp(Long basalTimestamp) {
        this.basalTimestamp = basalTimestamp;
    }

    public void setTempBasalString(String tempBasalString) {
        this.tempBasalString = tempBasalString;
    }

    public void setPumpTimestamp(Long pumpTimestamp) {
        this.pumpTimestamp = pumpTimestamp;
    }

    public void setPumpBattery(Integer pumpBattery) {
        this.pumpBattery = pumpBattery;
    }

    public void setPumpReservoir(Double pumpReservoir) {
        this.pumpReservoir = pumpReservoir;
    }

    public void setPumpStatus(String pumpStatus) {
        this.pumpStatus = pumpStatus;
    }
}
