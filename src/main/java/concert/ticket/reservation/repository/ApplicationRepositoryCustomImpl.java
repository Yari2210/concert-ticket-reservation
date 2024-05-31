package concert.ticket.reservation.repository;

import concert.ticket.reservation.Model.Concert;
import concert.ticket.reservation.config.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class ApplicationRepositoryCustomImpl extends BaseRepositoryCustom<Concert> implements ApplicationRepositoryCustom {
}