package concert.ticket.reservation.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateOnlySerializer extends StdSerializer<Date> {
    private static final long serialVersionUID = 1L;
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");

    public CustomDateOnlySerializer() {
        this((Class)null);
    }

    public CustomDateOnlySerializer(Class<Date> vc) {
        super(vc);
    }

    public void serialize(Date date, JsonGenerator jgen, SerializerProvider prov) throws IOException {
        if (date != null) {
            jgen.writeString(dateFormater.format(date));
        }

    }
}
