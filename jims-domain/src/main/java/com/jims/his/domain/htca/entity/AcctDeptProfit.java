package com.jims.his.domain.htca.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * AcctDeptProfit entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCT_DEPT_PROFIT", schema = "HTCA")
public class AcctDeptProfit implements java.io.Serializable {

	// Fields

	private String id;
	private String yearMonth;
	private String hospitalId;
	private Double deptIncome;
	private Double deptCost;
	private Double pleasedNum;
	private Double specialIncome;
    private String acctDeptId ;
    private Double deptLastIncome ;
    private Double convertRate ;
    private Double acctBalance ;
    private Double managerProfitCost;
    private Double managerStaffCost ;
    private Double incomeChangeItem;
    private Double costChangeItem ;
    private Double otherRewardsOrPunishments ;//其他奖惩
    //ALL_ROUND_INCOME
    private Double allRoundIncome ;//全项奖
    private String position ;//辅助排序想
    private Double managerCostMinus ;//管理成本减免



	// Constructors

	/** default constructor */
	public AcctDeptProfit() {
	}

	/** full constructor */
	public AcctDeptProfit(String yearMonth, String hospitalId,
                          Double deptIncome, Double deptCost, Double pleasedNum,
                          Double specialIncome, String acctDeptId, Double deptLastIncome, Double convertRate, Double acctBalance, Double managerProfitCost, Double managerStaffCost, Double incomeChangeItem, Double costChangeItem, Double otherRewardsOrPunishments, Double allRoundIncome, String position, Double managerCostMinus) {
		this.yearMonth = yearMonth;
		this.hospitalId = hospitalId;
		this.deptIncome = deptIncome;
		this.deptCost = deptCost;
		this.pleasedNum = pleasedNum;
		this.specialIncome = specialIncome;
        this.acctDeptId = acctDeptId;
        this.deptLastIncome = deptLastIncome;
        this.convertRate = convertRate;
        this.acctBalance = acctBalance;
        this.managerProfitCost = managerProfitCost;
        this.managerStaffCost = managerStaffCost;
        this.incomeChangeItem = incomeChangeItem;
        this.costChangeItem = costChangeItem;
        this.otherRewardsOrPunishments = otherRewardsOrPunishments;
        this.allRoundIncome = allRoundIncome;
        this.position = position;
        this.managerCostMinus = managerCostMinus;
    }

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "YEAR_MONTH", length = 20)
	public String getYearMonth() {
		return this.yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Column(name = "HOSPITAL_ID", length = 64)
	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	@Column(name = "DEPT_INCOME", precision = 22, scale = 0)
	public Double getDeptIncome() {
		return this.deptIncome;
	}

	public void setDeptIncome(Double deptIncome) {
		this.deptIncome = deptIncome;
	}

    @Column(name="dept_last_income")
    public Double getDeptLastIncome() {
        return deptLastIncome;
    }

    public void setDeptLastIncome(Double deptLastIncome) {
        this.deptLastIncome = deptLastIncome;
    }

    @Column(name = "DEPT_COST", precision = 22, scale = 0)
	public Double getDeptCost() {
		return this.deptCost;
	}

	public void setDeptCost(Double deptCost) {
		this.deptCost = deptCost;
	}

	@Column(name = "PLEASED_NUM", precision = 22, scale = 0)
	public Double getPleasedNum() {
		return this.pleasedNum;
	}

	public void setPleasedNum(Double pleasedNum) {
		this.pleasedNum = pleasedNum;
	}

	@Column(name = "SPECIAL_INCOME", precision = 22, scale = 0)
	public Double getSpecialIncome() {
		return this.specialIncome;
	}

	public void setSpecialIncome(Double specialIncome) {
		this.specialIncome = specialIncome;
	}


    @Column(name="acct_dept_id")
    public String getAcctDeptId() {
        return acctDeptId;
    }

    public void setAcctDeptId(String acctDeptId) {
        this.acctDeptId = acctDeptId;
    }

    @Column(name="convert_rate")
    public Double getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(Double convertRate) {
        this.convertRate = convertRate;
    }

    @Column(name="acct_balance")
    public Double getAcctBalance() {
        return acctBalance;
    }

    public void setAcctBalance(Double acctBalance) {
        this.acctBalance = acctBalance;
    }


    @Column(name="manager_profit_cost")
    public Double getManagerProfitCost() {
        return managerProfitCost;
    }

    public void setManagerProfitCost(Double managerProfitCost) {
        this.managerProfitCost = managerProfitCost;
    }

    @Column(name="all_round_income")
    public Double getAllRoundIncome() {
        return allRoundIncome;
    }

    public void setAllRoundIncome(Double allRoundIncome) {
        this.allRoundIncome = allRoundIncome;
    }

    @Column(name="income_change_item")
    public Double getIncomeChangeItem() {
        return incomeChangeItem;
    }

    public void setIncomeChangeItem(Double incomeChangeItem) {
        this.incomeChangeItem = incomeChangeItem;
    }

    @Column(name="manager_staff_cost")
    public Double getManagerStaffCost() {
        return managerStaffCost;
    }

    public void setManagerStaffCost(Double managerStaffCost) {
        this.managerStaffCost = managerStaffCost;
    }

    @Column(name="cost_change_item")
    public Double getCostChangeItem() {
        return costChangeItem;
    }

    public void setCostChangeItem(Double costChangeItem) {
        this.costChangeItem = costChangeItem;
    }

    @Transient
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Column(name="other_rewards_or_punishments")
    public Double getOtherRewardsOrPunishments() {
        return otherRewardsOrPunishments;
    }

    public void setOtherRewardsOrPunishments(Double otherRewardsOrPunishments) {
        this.otherRewardsOrPunishments = otherRewardsOrPunishments;
    }

    @Column(name="manager_cost_minus")
    public Double getManagerCostMinus() {
        return managerCostMinus;
    }

    public void setManagerCostMinus(Double managerCostMinus) {
        this.managerCostMinus = managerCostMinus;
    }
}