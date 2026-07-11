package com.app.enrollment.system.enrollment.server.domain.repository;

import com.app.enrollment.system.enrollment.server.domain.model.Section;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.SectionID;
import java.util.List;
import java.util.Optional;

/**
 * @author Alonso
 */
public interface SectionRepository {

  Section save(Section section);

  Optional<Section> findById(SectionID sectionID);

  List<Section> findAll();

}
