package com.github.filslo.simplesurveytool.dto;

public class AnswerResultsDTO {

    private int rating;
    private long count;

    public AnswerResultsDTO(int rating, long count) {
        this.rating = rating;
        this.count = count;
    }

    public AnswerResultsDTO() {}

    public int getRating() {
        return this.rating;
    }

    public long getCount() {
        return this.count;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String toString() {
        return "AnswerResultsDTO(rating=" + this.getRating() + ", count=" + this.getCount() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AnswerResultsDTO other)) return false;
        if (!other.canEqual( this)) return false;
        if (this.getRating() != other.getRating()) return false;
        return this.getCount() == other.getCount();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AnswerResultsDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getRating();
        final long $count = this.getCount();
        result = result * PRIME + Long.hashCode($count);
        return result;
    }
}
