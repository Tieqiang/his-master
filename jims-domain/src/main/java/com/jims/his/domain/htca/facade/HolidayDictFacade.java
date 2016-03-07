package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.htca.entity.HolidayDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2016/3/6.
 */
public class HolidayDictFacade extends BaseFacade {


    public List<HolidayDict> listHolidayDictByYearMonth(String yearMonth) {
        String hql = "from HolidayDict as dict where dict.holiday like '"+yearMonth+"%'" ;
        List<HolidayDict> holidayDicts = createQuery(HolidayDict.class,hql,new ArrayList<Object>()).getResultList() ;
        return holidayDicts;
    }

    @Transactional
    public HolidayDict deleteHolidayDict(String id){
        HolidayDict holidayDict = get(HolidayDict.class,id) ;
        remove(holidayDict);
        return holidayDict ;
    }

    @Transactional
    public List<HolidayDict> mergeHolidays(List<HolidayDict> holidayDicts){
        List<HolidayDict> holidayDictList = new ArrayList<>() ;

        for(HolidayDict holidayDict:holidayDicts){
            merge(holidayDict) ;
        }
        return holidayDictList ;
    }


}
