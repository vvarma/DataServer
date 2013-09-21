package com.nvr.data.repository;

import com.nvr.data.domain.Price;
import com.nvr.data.domain.Security;
import com.nvr.data.domain.SecurityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/14/13
 * Time: 12:55 AM
 * To change this template use File | Settings | File Templates.
 */

public interface SecurityJpaDao extends JpaRepository<Security,SecurityId>{

    @Query("select s from Security s join s.indiceList list where list.indexName=?1")
    List<Security> findByIndice(String indiceName);

    List<Security> findByPriced(Boolean priced);


    @Query("SELECT p FROM Price p join p.security s where s.symbol=?1 and s.series=?2 and p.priceDate between ?3 and ?4")
    List<Price> getSecurityPricesBetween(String symbol, String series, Date fromDate, Date toDate);
}
