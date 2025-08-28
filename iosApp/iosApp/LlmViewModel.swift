import SwiftUI
import Shared
import Foundation

@MainActor
class LlmViewModel: ObservableObject {
    private let inference: ILlmInference
    @Published var state: LlmState = LlmState.Initial()
    
    init() {
        self.inference = createInference()
    }
    
    func initialize() {
        if state is LlmState.Loading { return }
        
        Task {
            state = LlmState.Loading()
            
            do {
                try await inference.doInitModel(path: modelPath)

                state = LlmState.Ready()
            } catch {
                state = LlmState.Error()
            }
        }
    }
    
    func infer(input: String) {
        if state is LlmState.Loading || state is LlmState.Error || state is LlmState.Initial {
            return
        }
        
        Task {
            state = LlmState.Loading()
            
            do {
                let answer = try await inference.generateResponse(inputText: input)
                state = LlmState.Answer(answer: answer)
            } catch {
                state = LlmState.Answer(answer: "Oops! Something went wrong!")
            }
        }
    }
}