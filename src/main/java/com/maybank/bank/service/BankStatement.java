package com.maybank.bank.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.maybank.bank.dto.EmailDetails;
import com.maybank.bank.entity.Transaction;
import com.maybank.bank.entity.User;
import com.maybank.bank.repository.TransactionRepo;
import com.maybank.bank.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class BankStatement {

    private final TransactionRepo transactionRepo;
    private final UserRepo userRepo;
    private final EmailService emailService;
    private static final String FILE = "C:\\MyStatement.pdf";

    @Autowired
    public BankStatement(TransactionRepo transactionRepo, UserRepo userRepo, EmailService emailService) {
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {

        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactionList = transactionRepo.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isEqual(start))
                .filter(transaction -> transaction.getCreatedAt().isEqual(end))
                .toList();

        designStatement(transactionList, accountNumber, startDate, endDate);

        return transactionList;
    }

    private void designStatement(List<Transaction> transactions, String accountNumber, String startDate, String endDate) {

        User user = userRepo.findByAccountNumber(accountNumber);
        String username = user.getFirstName() + " " + user.getLastName();

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);

        log.info("Setting size of document");
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(FILE);
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException: ", e);
        }
        try {
            PdfWriter.getInstance(document, outputStream);
        } catch (DocumentException e) {
            log.error("DocumentException: ", e);
        }
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);

        PdfPCell bankName = new PdfPCell(new Phrase("Maybank Mock App"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.YELLOW);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("Petaling Jaya, Selangor"));
        bankAddress.setBorder(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfoTable = new PdfPTable(2);

        PdfPCell start = new PdfPCell(new Phrase("Start Date: " + startDate));
        start.setBorder(0);

        PdfPCell statement = new PdfPCell(new Phrase("ACCOUNT STATEMENT"));
        statement.setBorder(0);

        PdfPCell end = new PdfPCell(new Phrase("End Date: " + endDate));
        end.setBorder(0);

        PdfPCell customerName = new PdfPCell(new Phrase("Customer Name: " + username));
        customerName.setBorder(0);

        PdfPCell space = new PdfPCell();
        space.setBorder(0);

        PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.getAddress()));
        address.setBorder(0);

        PdfPTable transactionInfoTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("Date"));
        date.setBackgroundColor(BaseColor.YELLOW);
        date.setBorder(0);

        PdfPCell transactionType = new PdfPCell(new Phrase("Transaction Type"));
        transactionType.setBackgroundColor(BaseColor.YELLOW);
        transactionType.setBorder(0);

        PdfPCell transactionAmount = new PdfPCell(new Phrase("Transaction Amount"));
        transactionAmount.setBackgroundColor(BaseColor.YELLOW);
        transactionAmount.setBorder(0);

        PdfPCell status = new PdfPCell(new Phrase("Status"));
        status.setBackgroundColor(BaseColor.YELLOW);
        status.setBorder(0);

        transactionInfoTable.addCell(date);
        transactionInfoTable.addCell(transactionType);
        transactionInfoTable.addCell(transactionAmount);
        transactionInfoTable.addCell(status);
        transactions.forEach(transaction -> {
            transactionInfoTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionInfoTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionInfoTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionInfoTable.addCell(new Phrase(transaction.getStatus()));
        });

        statementInfoTable.addCell(start);
        statementInfoTable.addCell(statement);
        statementInfoTable.addCell(end);
        statementInfoTable.addCell(customerName);
        statementInfoTable.addCell(space);
        statementInfoTable.addCell(address);

        addTableToDocument(bankInfoTable, document);
        addTableToDocument(statementInfoTable, document);
        addTableToDocument(transactionInfoTable, document);

        document.close();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Statement of Account")
                .messageBody("Kindly save your requested account statement attached.")
                .attachment(FILE)
                .build();
        emailService.sendEmailWithAttachment(emailDetails);
    }

    private void addTableToDocument(PdfPTable table, Document document) {
        try {
            document.add(table);
        } catch (DocumentException e) {
            log.error("DocumentException: ", e);
        }
    }

}
