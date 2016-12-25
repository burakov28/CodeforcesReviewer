package com.ivan_pc.codeforcesreviewer.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Ivan-PC on 17.12.2016.
 */

public class Contest implements Parcelable {

    // Nullable means that show this field isn't necessary

    // Identificator of contest, is necessary to build a link for browser to open this contest
    public final int id;

    @NonNull
    public final String name;

    @NonNull
    //System of judging may be : CF, IOI, ICPC
    public final String type;

    @NonNull
    //Phase of contest may be : BEFORE, CODING, PENDING_SYSTEM_TEST, SYSTEM_TEST, FINISHED
    public final String phase;

    //in seconds
    public final long duration;

    //in Unix format, can be missed (equals to -1)
    public final long startTime;

    //can be missed (equals to -1) or negative
    //describe how many seconds passed after starting
    public final long relativeTime;

    @Nullable
    //Author's handle
    public final String preparedBy;

    @Nullable
    //website of contest
    public final String websiteURL;

    @Nullable
    public final String description;

    // from 1 to 5, if it is missing - it equals to 0
    public final int difficulty;

    @Nullable
    //kind of contest may be : Офиц. ACM-ICPC соревнование, Официальное школьное соревнование, Этап открытого кубка, Чемпионат школы/университета/города/области, Соревнование со сборов, Офиц. международное инд. соревнование, Тренировочное соревнование.
    public final String kind;

    @Nullable
    //Region if it is ICPC
    public final String icpcRegion;

    @Nullable
    public final String country;

    @Nullable
    public final String city;

    @Nullable
    public final String season;

    public Contest(int id, @NonNull String name, @NonNull String type, @NonNull String phase,
                   long duration, long startTime, long relativeTime, @Nullable String preparedBy,
                   @Nullable String websiteURL, @Nullable String description, int difficulty,
                   @Nullable String kind, @Nullable String icpcRegion, @Nullable String country,
                   @Nullable String city, @Nullable String season) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.phase = phase;
        this.duration = duration;
        this.startTime = startTime;
        this.relativeTime = relativeTime;
        this.preparedBy = preparedBy;
        this.websiteURL = websiteURL;
        this.description = description;
        this.difficulty = difficulty;
        this.kind = kind;
        this.icpcRegion = icpcRegion;
        this.country = country;
        this.city = city;
        this.season = season;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(phase);
        parcel.writeLong(duration);
        parcel.writeLong(startTime);
        parcel.writeLong(relativeTime);
        parcel.writeString(preparedBy);
        parcel.writeString(websiteURL);
        parcel.writeString(description);
        parcel.writeInt(difficulty);
        parcel.writeString(kind);
        parcel.writeString(icpcRegion);
        parcel.writeString(country);
        parcel.writeString(city);
        parcel.writeString(season);
    }

    private Contest(Parcel parcel) {
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.type = parcel.readString();
        this.phase = parcel.readString();
        this.duration = parcel.readLong();
        this.startTime = parcel.readLong();
        this.relativeTime = parcel.readLong();
        this.preparedBy = parcel.readString();
        this.websiteURL = parcel.readString();
        this.description = parcel.readString();
        this.difficulty = parcel.readInt();
        this.kind = parcel.readString();
        this.icpcRegion = parcel.readString();
        this.country = parcel.readString();
        this.city = parcel.readString();
        this.season = parcel.readString();
    }

    public static final Parcelable.Creator<Contest> CREATOR = new Parcelable.Creator<Contest>() {

        @Override
        public Contest createFromParcel(Parcel parcel) {
            return new Contest(parcel);
        }

        @Override
        public Contest[] newArray(int size) {
            return new Contest[size];
        }
    };
}
