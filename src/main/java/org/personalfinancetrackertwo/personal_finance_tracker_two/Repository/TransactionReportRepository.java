package org.personalfinancetrackertwo.personal_finance_tracker_two.Repository;

import org.personalfinancetrackertwo.personal_finance_tracker_two.Entity.TransactionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionReportRepository extends JpaRepository<TransactionReport, Long> {
}
