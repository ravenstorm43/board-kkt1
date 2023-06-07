package idusw.springboot.controller;

import idusw.springboot.domain.Board;
import idusw.springboot.domain.Member;
import idusw.springboot.domain.PageRequestDTO;
import idusw.springboot.service.BoardService;
import idusw.springboot.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/boards")
public class BoardController {
    // 생성자 주입 : Spring Framework <- AutoWired (필드 주입)
    private final BoardService boardService;
    MemberService memberService;
    public void MemberController(MemberService memberService) { // Spring Framework이 주입(하도록 요청함)
        this.memberService = memberService;
    }
    HttpSession session = null;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/reg-form")
    public String getRegForm(Model model, HttpServletRequest request) {
        session = request.getSession();
        Member member = (Member) session.getAttribute("mb");
        if (member != null) {
            model.addAttribute("board", Board.builder().build());
            return "/boards/reg-form";
        } else
            return "redirect:/members/login-form";
    }

    @PostMapping("")
    public String postBoard(@ModelAttribute("board") Board dto, Model model, HttpServletRequest request) {
        session = request.getSession();
        Member member = (Member) session.getAttribute("mb");
        if(member != null) {
            // form에서 hidden 전송하는 방식으로 변경
            dto.setWriterSeq(member.getSeq());
            dto.setWriterEmail(member.getEmail());
            dto.setWriterName(member.getName());

            Long bno = Long.valueOf(boardService.registerBoard(dto));

            return "redirect:/boards"; // 등록 후 목록 보기
        } else
            return  "redirect:/members/login-form";
    }

    @GetMapping("")
    public String getBoards(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("list", boardService.findBoardAll(pageRequestDTO));
        return "/boards/list";
    }

    @GetMapping("/{bno}")
    public String getBoardByBno(@PathVariable("bno") Long bno, Model model) {
        Board board = boardService.findBoardById(Board.builder().bno(bno).build());
        boardService.updateBoard(board);
        model.addAttribute("board", boardService.findBoardById(board));
        return "/boards/detail";
    }

    @GetMapping("/{bno}/up-form")
    public String getUpForm(@PathVariable("bno") Long bno, Model model) {
        Board board = boardService.findBoardById(Board.builder().bno(bno).build());
        model.addAttribute("board", board);
        return "/boards/upform";
    }

    @PutMapping("/{bno}")
    public String putBoard(@ModelAttribute("board") Board board, Model model) {
        boardService.updateBoard(board);
        model.addAttribute(boardService.findBoardById(board));
        return "redirect:/boards/" + board.getBno();
    }

    @GetMapping("/{bno}/del-form")
    public String getDelForm(@PathVariable("bno") Long bno, Model model) {
        Board board = boardService.findBoardById(Board.builder().bno(bno).build());
        model.addAttribute("board", board);
        return "/boards/del-form";
    }

    @DeleteMapping("/{bno}")
    public String deleteBoard(@ModelAttribute("board") Board board, Model model) {
        boardService.deleteBoard(board);
        model.addAttribute(board);
        return "redirect:/boards";
    }

    @PostMapping("/")
    public String createMember(@ModelAttribute("board") Board board, Model model) {
        if (boardService.registerBoard(board) > 0)
            return "redirect:/boards";
        else
            return "/errors/404";
    }
}
