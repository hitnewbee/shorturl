import play.http.DefaultHttpFilters;

import javax.inject.Inject;

public class Filters extends DefaultHttpFilters {
    @Inject
    public Filters(LoggingFilter loggingFilter){
        super(loggingFilter);
    }
}
