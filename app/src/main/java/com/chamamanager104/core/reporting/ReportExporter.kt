package com.chamamanager104.core.reporting

import android.content.Context
import com.chamamanager104.core.model.Contribution
import com.chamamanager104.core.model.Member
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportExporter @Inject constructor() {

    fun exportMembersCsv(context: Context, members: List<Member>): File {
        val file = File(context.cacheDir, "members_report.csv")
        val header = "member_id,full_name,phone,email,total_contribution,outstanding_loan\n"
        val body = members.joinToString("\n") { member ->
            listOf(
                member.id,
                member.fullName,
                member.phoneNumber,
                member.email,
                member.totalContribution,
                member.outstandingLoan
            ).joinToString(",")
        }
        file.writeText(header + body)
        return file
    }

    fun exportContributionStatement(
        context: Context,
        member: Member,
        contributions: List<Contribution>
    ): File {
        val file = File(context.cacheDir, "statement_${member.id}.csv")
        val header = "member,amount,frequency,paid_on,due_on,method,receipt\n"
        val body = contributions.joinToString("\n") { item ->
            listOf(
                member.fullName,
                item.amount,
                item.frequency.name,
                item.paidOn,
                item.dueOn,
                item.method,
                item.receiptNumber
            ).joinToString(",")
        }
        file.writeText(header + body)
        return file
    }
}
