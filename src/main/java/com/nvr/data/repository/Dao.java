package com.nvr.data.repository;

import com.nvr.data.domain.Security;
import com.nvr.data.domain.SecurityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: vvarma
 * Date: 9/14/13
 * Time: 1:03 AM
 * To change this template use File | Settings | File Templates.
 */

public interface Dao extends JpaRepository<Security,SecurityId> {

}
