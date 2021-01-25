/*
 * Copyright 2020 Quini Roiz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 *  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

package es.roiz.basiccore.common.validation.impl;

import es.roiz.basiccore.common.validation.Adult;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AdultValidation implements
        ConstraintValidator<Adult, Object> {

    @Override
    public void initialize(Adult adult) {
    }

    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext cxt) {
        if (value == null) {
            return false;
        }

        TimeZone tz;
        ZoneId zid;
        LocalDate valueLocalDate;

        if (value instanceof Calendar) {
            tz = ((Calendar) value).getTimeZone();
            zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
            valueLocalDate = LocalDateTime.ofInstant(((Calendar) value).toInstant(), zid).toLocalDate();
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((Date) value).getTime());
            tz = calendar.getTimeZone();
            zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
            valueLocalDate = LocalDateTime.ofInstant(calendar.toInstant(), zid).toLocalDate();
        }

        LocalDate todayLocalDate = LocalDateTime.ofInstant(Calendar.getInstance().toInstant(), zid).toLocalDate();
        int years = Period.between(valueLocalDate, todayLocalDate).getYears();
        return years >= 18;
    }

}
