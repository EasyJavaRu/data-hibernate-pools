package ru.easyjava.data.hibernate.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyGroup;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Single financial operation.
 */
@SuppressWarnings("PMD")
@ToString
@Entity
@Table(name = "journal",
        indexes = {@Index(
                name = "j_account_idx",
                columnList = "accountId", unique = false)},
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"id", "accountId"})})
@SecondaryTable(name = "operations_details",
        pkJoinColumns = @PrimaryKeyJoinColumn(
                name = "op_id",
                referencedColumnName = "id"))
public class Operation {
    /**
     * Operation id.
     */
    @Id
    @GeneratedValue
    @Getter
    @Setter
    @Column(name = "id", nullable = false, updatable = false)
    private Long rowId;

    /**
     * Related transaction id.
     *
     * Single transaction could have
     * more then one operations.
     */
    @Getter
    @Setter
    @Column(name = "trxId", nullable = false, updatable = false)
    private Long id;

    /**
     * Operation's account.
     */
    @Getter
    @Setter
    @Column(nullable = false, updatable = false)
    private Integer accountId;

    /**
     * Operation's amount.
     */
    @Getter
    @Setter
    @Column(nullable = false, updatable = false, scale = 2, precision = 10)
    @Basic( fetch = FetchType.LAZY )
    private BigDecimal amount;

    /**
     * Operation's timestamp.
     */
    @Getter
    @Setter
    @Column(nullable = false, updatable = false)
    private ZonedDateTime timestamp;

    /**
     * Optional operation description.
     */
    @Getter
    @Setter
    @Column(table = "operations_details", length = 64)
    @Basic( fetch = FetchType.LAZY )
    @LazyGroup("text")
    private String description;

    /**
     * Optional operation code.
     */
    @Getter
    @Setter
    @Column(table = "operations_details")
    private Integer opCode;

}
