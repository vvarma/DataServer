package com.nvr.data.service;

import com.nvr.data.domain.Indice;
import com.nvr.data.domain.Security;
import com.nvr.data.service.annotation.AppService;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/15/13
 * Time: 4:26 AM
 * To change this template use File | Settings | File Templates.
 */

public interface IndexService {
    public List<Indice> getAllIndice();
    public List<Security> getAllSecurity(Indice indice);
}
